local State = {
    Created = 1,
    Initialize = 2,
    Appear = 3,
    Disappear = 4,
    Disposed = 5,
}
---@class MvpContext
local M = class("MvpContext")

function M:ctor(presenter, model)
    ---@type Presenter
    self.presenter = presenter
    self.model = model
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

return M