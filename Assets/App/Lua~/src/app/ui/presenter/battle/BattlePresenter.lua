---@class BattlePresenter: Presenter
---@field _view BattleView
local M = class("BattlePresenter", require("app.ui.base.mvp.Presenter"))

function M:didAppeared()
    local topPart = self._view.topPartView
    self.disposable:append(topPart.deckEvent:subscribe(self.onTopDeck, self))
    self.disposable:append(topPart.mapEvent:subscribe(self.onTopMap, self))
    self.disposable:append(topPart.settingEvent:subscribe(self.onTopSetting, self))
end

function M:onTopDeck()
    log.debug('top deck')
end

function M:onTopMap()
    log.debug('top map')
end

function M:onTopSetting()
    log.debug('top setting')
end

return M
