---@class CharacterSelectPresenter : Presenter
---@field _view CharacterSelectView
local M = class("CharacterSelectPresenter", require("app.ui.base.mvp.Presenter"))

function M:didAppeared()
    self._view.backEvent:subscribe(self.onBackKey, self)
    self._view.startEvent:subscribe(self.onStart, self)
    self._view.characterSelectEvent:subscribe(self.onCharacterSelected, self)
end

function M:onStart()
    log.debug('onStart')
end

function M:onCharacterSelected(characterName)
    log.debug('onCharacterSelected {}', characterName)
    self._view:selectCharacter(characterName)
end

return M