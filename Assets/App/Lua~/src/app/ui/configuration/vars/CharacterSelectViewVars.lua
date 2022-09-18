--
-- Auto generated, do not edit manually
-- date 2022-09-15 23:21:16
--
return {
    attach = function(binder)
        return {
            Transforms = {
                characterInfo = binder.Transforms[0],
                characterPoster = binder.Transforms[1],
                characterName = binder.Transforms[2],
                hpText = binder.Transforms[3],
                goldText = binder.Transforms[4],
                title = binder.Transforms[5],
                relicsIcon = binder.Transforms[6],
                relicsName = binder.Transforms[7],
                relicsDescription = binder.Transforms[8],
                btnBack = binder.Transforms[9],
                btnStart = binder.Transforms[10],
                buttonIron = binder.Transforms[11],
                buttonSilent = binder.Transforms[12],
            },
            GameObjects = {
                characterInfo = binder.GameObjects[0],
                characterPoster = binder.GameObjects[1],
                characterName = binder.GameObjects[2],
                hpText = binder.GameObjects[3],
                goldText = binder.GameObjects[4],
                title = binder.GameObjects[5],
                relicsIcon = binder.GameObjects[6],
                relicsName = binder.GameObjects[7],
                relicsDescription = binder.GameObjects[8],
                btnBack = binder.GameObjects[9],
                btnStart = binder.GameObjects[10],
                buttonIron = binder.GameObjects[11],
                buttonSilent = binder.GameObjects[12],
            },
            Texts = {
                characterName = binder.Texts[0],
                hpText = binder.Texts[1],
                goldText = binder.Texts[2],
                title = binder.Texts[3],
                relicsName = binder.Texts[4],
                relicsDescription = binder.Texts[5],
            },
            Images = {
                characterPoster = binder.Images[0],
                relicsIcon = binder.Images[1],
            },
            ButtonViews = {
                btnBack = binder.ButtonViews[0],
                btnStart = binder.ButtonViews[1],
                buttonIron = binder.ButtonViews[2],
                buttonSilent = binder.ButtonViews[3],
            },
        }
    end
}
