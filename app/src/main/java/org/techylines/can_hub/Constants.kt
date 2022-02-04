package org.techylines.can_hub

const val TAG = "CANHub"

const val SERVICE_CHANNEL_ID = "HubServiceChannel"

const val ACTION_USB_SERIAL_DEVICE_ATTACH = "org.techylines.action.USB_SERIAL_DEVICE_ATTACH"
const val ACTION_USB_SERIAL_DEVICE_CONNECT = "org.techylines.action.USB_SERIAL_DEVICE_CONNECT"
const val ACTION_USB_SERIAL_DEVICE_DISCONNECT = "org.techylines.action.USB_SERIAL_DEVICE_DISCONNECT"
const val ACTION_USB_SERIAL_DEVICE_REMOVE = "org.techylines.action.USB_SERIAL_DEVICE_REMOVE"
const val ACTION_UI_DEVICE_CONNECT = "org.techylines.action.UI_DEVICE_CONNECT"
const val ACTION_UI_DEVICE_DISCONNECT = "org.techylines.action.UI_DEVICE_DISCONNECT"
const val ACTION_UI_DEVICE_CONFIGURE = "org.techylines.action.UI_DEVICE_CONFIGURE"
const val EXTRA_USB_DEVICE_ID = "org.techylines.EXTRA_USB_DEVICE_ID"
const val EXTRA_PENDING_INTENT = "org.techylines.EXTRA_PENDING_INTENT"
const val EXTRA_ACQUIRE_PERMISSION = "org.techylines.EXTRA_ACQUIRE_PERMISSION"

const val HEARTBEAT_DETACHED_PAYLOAD = "0000000000000000"
const val HEARTBEAT_DISCONNECTED_PAYLOAD = "0100000000000000"
const val HEARTBEAT_CONNECTED_PAYLOAD = "0300000000000000"
