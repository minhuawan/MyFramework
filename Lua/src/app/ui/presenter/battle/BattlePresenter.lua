---@class BattlePresenter: Presenter
---@field _view BattleView
local M = class("BattlePresenter", require("app.ui.base.mvp.Presenter"))

function M:didAppeared()
    self._view.topPartView.deckEvent:subscribe(self.onTopDeck, self)
    self._view.topPartView.mapEvent:subscribe(self.onTopMap, self)
    self._view.topPartView.settingEvent:subscribe(self.onTopSetting, self)
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
