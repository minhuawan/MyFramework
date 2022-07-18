local State = {
    Created = 1,
    Initialize = 2,
    Appear = 3,
    Disappear = 4,
    Disposed = 5,
}
---@class MvpContext
local M = class("MvpContext")

---@param configuration table
function M:ctor(configuration)
    if not configuration then
        log.exception("configuration is nil value")
    end
    self.configuration = configuration
    self.presenter = configuration.mvp.presenter.new()
    if configuration.mvp.model then
        self.model = configuration.mvp.model.new()
    end
    self._state = State.Created

    if not self.presenter then
        log.exception("presenter is nil value")
    end
end

function M:moveNextState()
    if self._state == State.Disposed then
        log.exception(
                "move next state error, state are disposed, presenter type {}",
                self.presenter.class.__cname
        )
    end
    self._state = self._state + 1
    if self._state == State.Initialize then
        self.presenter:initialize(self)
    elseif self._state == State.Appear then
        self.presenter:appear()
    elseif self._state == State.Disappear then
        self.presenter:disappear()
    elseif self._state == State.Disposed then
        self.presenter:dispose()
    else
        log.exception("move next state error, state is {}", self._state)
    end
end

function M:createViewAsync(next)
    if not self.presenter then
        log.exception("createViewAsync error, presenter is nil")
    end
    local prefab = self.configuration.prefab
    log.assert(type(prefab) == "string" and prefab ~= "", "prefab is nil or empty")
    -- todo CS load
    local gameObject
    gameObject:SetActive(false)
    next(gameObject)
end

return M