# PixelPhysics

Our goal is to make a realtime physics simulation built around pixels, each pixel is one unit of a substance and there are different substances that behave differently and can interact with each other. Some substances create other ones, like fire creates smoke, and some substances can combine into ones with different properties, like water + sand = wet sand.

List of planned substances:  
Sand (falls downwards or diagonally),
Soil (acts like sand, but heavier),
Water (falls down, diagonally, or moves left/right),  
Stone (does nothing),  
Metal (conducts electricity),  
Wet sand (created with sand+water, can stack at a higher angle than sand),  
Fire (burns flammables, produces smoke),  
Smoke (rises),  
Wood (can burn),  
Plants (grow on sand?),  
Lava (burns flammables and melts through stone/metal),

We plan on using java because most of the team knows it and it has a library for simple 2d rendering.
Other than that we aren't using any other libraries or dependencies.

The user experience should be similar to a sandbox combined with a chemistry lab, where the user can experiment with mixing different substances to see their interactions. Each substance is based off of simple rules, but they can be combined to produce complex behavior (like a rube goldberg machine).

In the future if that can be completed, it would be interesting to turn it into some sort of game.

Trello: https://trello.com/b/vTjLOnEO/pixel-physics
