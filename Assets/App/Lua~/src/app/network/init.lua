local ProtocolManager = require("app.network.protocol.ProtocolManager")
return {
    protocol = {
        ProtocolManager = ProtocolManager() -- create instance
    }
}