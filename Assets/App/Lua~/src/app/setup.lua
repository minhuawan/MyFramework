require("core.class")
require("core.formatter")
require("core.json")
require("core.pb.setup")
require("core.log")
require("core.device")
require("core.collections.setup")
require("core.calendar")
--require("core.storage")
--require("core.event")
--require("core.resources")



app = {
    device = require("core.device"),
    storage = require("core.storage"),
    resources = require("core.resources"),
}


-- require modules before global
require("core.globals")



-- load all pb schemas
protoc.loadall(true)
