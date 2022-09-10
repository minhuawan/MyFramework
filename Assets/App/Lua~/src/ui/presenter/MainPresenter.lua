local UIManager = require("core.ui.UIManager")
local Presenter = require("core.ui.mvp.Presenter")
---@class MainPresenter : Presenter
---@field _view MainView
local M = class("MainPresenter", Presenter)

function M:didAppeared()
    self._view.editEvent:subscribe(bind(self.onEdit, self))
    self._view.startEvent:subscribe(bind(self.onStart, self))
    self._view.wikiEvent:subscribe(bind(self.onWiki, self))
    self._view.analysisEvent:subscribe(bind(self.onAnalysis, self))
    self._view.settingEvent:subscribe(bind(self.onSetting, self))
    self._view.patchEvent:subscribe(bind(self.onPatch, self))
    self._view.exitEvent:subscribe(bind(self.onExit, self))
end

function M:onEdit()
    log.debug('edit todo')
end

function M:onStart()
    local configuration = require("ui.configuration.context.start")
    UIManager:navigateTo(configuration)
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
