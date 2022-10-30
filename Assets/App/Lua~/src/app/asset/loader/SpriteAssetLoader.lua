---@class SpriteAssetLoader
local M = class("SpriteAssetLoader")

function M:ctor(path)
    self.path = path
    self.sprite = nil
    self.callback = nil
    self.done = false
end

function M:load(callback)
    if self.done then
        callback(self)
        return
    end
    self.callback = callback
    self.guid = CS.MyFramework.Runtime.Utils.AssetLoader.LoadSprite(self.path, function(sprite)
        if sprite == nil then
            log.error("load sprite failed, path: `{}`", self.path)
        end
        self.sprite = sprite
        self.done = true
        self.callback(self)
    end)
end

function M:isDone()
    return self.done
end

function M:getAsset()
    return self.sprite
end

function M:dispose()
    -- todo 这里要自己销毁 asset 吗？
    if self.guid then
        CS.MyFramework.Runtime.Utils.AssetLoader.RemoveDelegateByGuid(self.guid)
        self.guid = nil
    end
end



return M