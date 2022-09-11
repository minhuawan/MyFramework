--
-- Auto generated, do not edit manually
-- date 2022-09-11 21:34:17
--
return {
    attach = function(binder)
        return {
            Transforms = {
                characterPoster = binder.Transforms[0],
                characterName = binder.Transforms[1],
                hpText = binder.Transforms[2],
                goldText = binder.Transforms[3],
                description = binder.Transforms[4],
                relicsIcon = binder.Transforms[5],
                relicsName = binder.Transforms[6],
                relicsDescription = binder.Transforms[7],
                btnBack = binder.Transforms[8],
                btnStart = binder.Transforms[9],
                buttonIron = binder.Transforms[10],
            },
            GameObjects = {
                characterPoster = binder.GameObjects[0],
                characterName = binder.GameObjects[1],
                hpText = binder.GameObjects[2],
                goldText = binder.GameObjects[3],
                description = binder.GameObjects[4],
                relicsIcon = binder.GameObjects[5],
                relicsName = binder.GameObjects[6],
                relicsDescription = binder.GameObjects[7],
                btnBack = binder.GameObjects[8],
                btnStart = binder.GameObjects[9],
                buttonIron = binder.GameObjects[10],
            },
            Texts = {
                characterName = binder.Texts[0],
                hpText = binder.Texts[1],
                goldText = binder.Texts[2],
                description = binder.Texts[3],
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
            },
        }
    end
}
