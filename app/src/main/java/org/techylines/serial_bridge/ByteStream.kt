package org.techylines.serial_bridge

import android.util.Log
import java.io.Closeable
import java.nio.ByteBuffer

// Interface for things that can be closed. This does not directly match Java's Closeable()
// because we require a way for checking if something has been closed.
interface Closer {
    // Close the object. This is idempotent; calling this multiple times will not result in
    // an error. An error is only returned if the object is not closed after the call completes.
    fun close(): Throwable?

    // Return true if the object is closed. Should return true after the first call to close().
    fun isClosed(): Boolean
}

// Interface for objects that read bytes from a stream.
interface ByteReader : Closer, Iterable<Byte> {
    // Read bytes into the provided buffer. Return an error on failure or the number
    // of bytes read into the buffer. Timeouts are not errors. If a timeout occurs then no bytes
    // are read into the buffer and 0 is returned.
    fun read(bytes: ByteArray): Result<Int>

    // Return the ideal read buffer size in bytes. 4096 is the default.
    fun getReadBufferSize(): Int { return 4096 }
}

// Iterator which returns bytes from a ByteReader.
class ByteReaderIterator(private val reader: ByteReader) : Iterator<Byte> {
    private val buffer: ByteBuffer = ByteBuffer.allocate(reader.getReadBufferSize())

    init {
        buffer.limit(0)
    }

    // Get the next element. Throws a NoSuchElementException if the reader is closed.
    override operator fun next(): Byte {
        return buffer.get()
    }

    // Check if there's another element to read. Return false when the reader is closed.
    override operator fun hasNext(): Boolean {
        return buffer.remaining() > 0 || (!reader.isClosed() && fetch())
    }

    // Check if there are no more bytes in the buffer and read more if so necessary.
    private fun fetch(): Boolean {
        while (buffer.remaining() == 0) {
            if (reader.isClosed()) {
                return false
            }
            val result = reader.read(buffer.array())
            buffer.rewind()
            buffer.limit(result.getOrNull() ?: 0)
            if (result.isFailure) {
                Log.w(TAG, "reader error, will retry: ${result.exceptionOrNull()}")
            }
        }
        return true
    }
}

// Interface for objects that write bytes to a stream.
interface ByteWriter : Closer {
    // Write bytes from the buffer and return the number of bytes written. It is allowable for
    // this method to not write all of the bytes in the buffer. In such a case a subsequent call
    // to write may be made to write the remaining bytes. An error is returned on failure. Timeouts
    // are not errors. If a timeout occurs then the number of bytes written is returned and
    // subsequent calls to write may be made.
    fun write(bytes: ByteArray): Result<Int>
}

// Interface for objects that read and write bytes to a stream.
interface ByteReaderWriter : ByteReader, ByteWriter

// General stream interface.
abstract class ByteStream : Closeable {
    // Return the name of the stream. This should be unique.
    abstract fun getName(): String

    // Read bytes from the stream.
    abstract fun read(bytes: ByteArray): Result<Int>

    // Write bytes to the stream.
    abstract fun write(bytes: ByteArray): Throwable?

    // Construct a read buffer of an ideal size.
    open fun makeReadBuffer(): ByteArray {
        return ByteArray(4096)
    }

    // Return true if the stream is open.
    abstract fun isOpen(): Boolean
}