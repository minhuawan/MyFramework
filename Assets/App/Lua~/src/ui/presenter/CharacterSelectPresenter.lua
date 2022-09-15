---@class CharacterSelectPresenter : Presenter
---@field _view CharacterSelectView
local M = class("CharacterSelectPresenter", require("core.ui.mvp.Presenter"))

function M:didAppeared()
    self._view.backEvent:subscribe(bind(self.onBackKey, self))
    self._view.startEvent:subscribe(bind(self.onStart, self))
    self._view.characterSelectEvent:subscribe(bind(self.onCharacterSelected, self))
end

function M:onStart()
    log.debug('onStart')
end

function M:onCharacterSelected(characterName)
    self._view:selectCharacter(characterName)
end

return M