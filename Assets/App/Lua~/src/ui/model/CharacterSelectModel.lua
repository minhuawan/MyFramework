---@class CharacterSelectModel : Model
local M = class("CharacterSelectModel", require("core.ui.mvp.Model"))

function M:getPosterPath()
    -- todo
    return ''
end

function M:getName()
    return '铁甲战士'
end

function M:getHpText()
    return '生命:   80/80'
end

function M:getGoldText()
    return '金币:   99'
end

function M:getDescription()
    return '铁甲军团残留下的士兵。\n他出卖自己的灵魂，获得了恶魔的力量。'
end

function M:getRelicsIconPath()
    -- todo
    return ''
end

function M:getRelicsName()
    return '燃烧之血'
end

function M:getRelicsDescription()
    return '在战斗结束时，回复<color="#84C9E6">6</color>点生命。'
end

return M