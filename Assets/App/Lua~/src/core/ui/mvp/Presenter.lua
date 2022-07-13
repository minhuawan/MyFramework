local M = class("Presenter")

function M:ctor()
    self.view = nil
end

function M:initialize(mvpContext)
end

function M:didAppeared()
    if self.view then
        self.view.gameObject:SetActive(true)
    end
end

function M:onBackKey()

end

function M.InstantiateViewAsync(mvpContext, next)
    if not mvpContext then
        return
    end
    if not mvpContext.presenter then
        return
    end
end

return M