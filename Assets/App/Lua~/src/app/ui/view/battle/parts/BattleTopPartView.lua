---@class BattleTopPartView
local M = class("BattleTopPartView")

---@param mainView BattleView
function M:ctor(mainView)
    self.mainView = mainView

    local vars = mainView.vars
    self.txtUserName = vars.Texts.userName
    self.txtCharacterName = vars.Texts.characterName
    self.txtHp = vars.Texts.hpText
    self.txtGold = vars.Texts.goldText

    self.potions = {
        vars.Images.potion1,
        vars.Images.potion2,
        vars.Images.potion3,
    }

    self.mapEvent = vars.ButtonViews.map.clickEvent
    self.deckEvent = vars.ButtonViews.deck.clickEvent
    self.settingEvent = vars.ButtonViews.setting.clickEvent
end

-- todo
function M:onHealthChanged()

end

return M