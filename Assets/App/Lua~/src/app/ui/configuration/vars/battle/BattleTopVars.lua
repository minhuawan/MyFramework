--
-- date: 2022-12-13 17:11:10
-- file: Assets/AppData/Scene/main.unity
-- Auto generated, do not edit manually
--
local __return__
local ButtonViewWrap = require('app.ui.base.ButtonViewWrap')
return {
    attach = function(binder)
        __return__ = {
            ButtonViews = {
                setting = ButtonViewWrap(binder.ButtonViews[0]),
                deck = ButtonViewWrap(binder.ButtonViews[1]),
                map = ButtonViewWrap(binder.ButtonViews[2]),
            },
            Binders = {
              binder1 = {

              }
            },
            dispose = function()
                __return__.ButtonViews.setting:dispose()
                __return__.ButtonViews.deck:dispose()
                __return__.ButtonViews.map:dispose()
            end,
        }
        return __return__
    end,
}
