# Cool Picture Effect thing
![Video](vide.mp4) AAAA doesnt work

This is a cool side project.

## History

One of my first project trying to render images/videos  using Java was a small test where I had an array of points and colors and every pixel was set to the corresponding color of the closest point.  

Fast forward a year and I see this and wander if it is possible to set the color of every point to the color of the image at that point I did it quickly but with the amount of points I had it didn't look very interesting so I tried increasing it to 2000 point but it was too slow

## Optimizing

My ferst step to try to make things work was pre-rendering all the frames, because it took around 4 seconds to generate a frame and that was too slow to make believable movement, but it still took years to render.

I tried a few things but settled using the `NeighborGrid.java` to sepparate the points into local groups so each pixel instead of looking through 2000 points it looked at around 10, and that made it mucuh faster.  

What this did is separate the picture into more little pictures and process each one and that was faster, but it generated an artifact, let me explain:  
The whole picture was separated into a grid when a pixel is close to the border it will only check the points inside the cell but the closest point might be in another cell.
```
╔════╦════╗
║    ║   b║
║   a║ .  ║
╠════╬════╣
║    ║    ║
║ c  ║   d║
╚════╩════╝
```
Here the closest point is `a` but if you only search the cell you will get `b`.

So how did I fix this? I added something I call 'overflow' It essentally means the true bounding box of a cell.
```
┌────────────────┐
│                │
│    ╔══════╗    │
│    ║      ║    │
│    ║      ║    │
│    ╚══════╝    │
│                │
└────────────────┘
═ Cell
─ Activation Radius
```
So you choose an overflow (%) that will eliminate most of the artefacts.
