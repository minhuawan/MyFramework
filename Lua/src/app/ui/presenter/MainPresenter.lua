local Presenter = require("app.ui.base.mvp.Presenter")
---@class MainPresenter : Presenter
---@field _view MainView
local M = class("MainPresenter", Presenter)

function M:didAppeared()
    self._view.editEvent:subscribe(self.onEdit, self)
    self._view.startEvent:subscribe(self.onStart, self)
    self._view.wikiEvent:subscribe(self.onWiki, self)
    self._view.AnalysisEvent:subscribe(self.onAnalysis, self)
    self._view.settingEvent:subscribe(self.onSetting, self)
    self._view.patchEvent:subscribe(self.onPatch, self)
    self._view.exitEvent:subscribe(self.onExit, self)
end

function M:onEdit()
    log.debug('edit todo')
end

function M:onStart()
    local configuration = require("app.ui.configuration.context.StartContext")
    App.ui.UIManager:navigateTo(configuration)
end

function M:onWiki()
    log.debug('wiki')
end

function M:onAnalysis()
    log.debug('analysis')
end

function M:onSetting()
    log.debug('setting')
end

function M:onPatch()
    log.debug('patch')
end

function M:onExit()
    log.debug('exit')
end

return M
