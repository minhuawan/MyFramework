local Presenter = require("app.ui.base.mvp.Presenter")
---@class MainPresenter : Presenter
---@field _view MainView
local M = class("MainPresenter", Presenter)

function M:didAppeared()
    self.disposable:append(self._view.btnEdit.clickEvent:subscribe(bind(self.onEdit, self)))
    self.disposable:append(self._view.btnStart.clickEvent:subscribe(bind(self.onStart, self)))
    self.disposable:append(self._view.btnWiki.clickEvent:subscribe(bind(self.onWiki, self)))
    self.disposable:append(self._view.btnAnalysis.clickEvent:subscribe(bind(self.onAnalysis, self)))
    self.disposable:append(self._view.btnSetting.clickEvent:subscribe(bind(self.onSetting, self)))
    self.disposable:append(self._view.btnPatch.clickEvent:subscribe(bind(self.onPatch, self)))
    self.disposable:append(self._view.btnExit.clickEvent:subscribe(bind(self.onExit, self)))
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
