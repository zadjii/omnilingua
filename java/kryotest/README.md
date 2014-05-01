# kryotest - multiplayer application

This it a really rudimentary implementation of the fantastic [Fast-Paced Multiplayer Tutorial](http://www.gabrielgambetta.com/fpm1.html) by Gabriel Gambetta. I used [kryonet](https://github.com/EsotericSoftware/kryonet) for the client-server communications and [Slick2D](http://slick.ninjacave.com/) for the rendering.

When I say "rudimentary", I mean hacky as hell. It's some of the sloppiest code I think I've __ever__ written. Methods are placed haphazardly all over the files. Some things are static, others instance members, with no real rhyme or reason. Encapsulation? LOL. The netcode and the actual engine code are way too closely intertwined for my tastes. Oh, and I seriously doubt there's anything useful anywhere in the comments.

No person should even be expected to use this code, *ever*.

That being said, it works. And it's really fucking cool. So I'm putting it on the internet.

## Where to go from here:
This code can obviously be better. The network communications can be optimized, etc. Here are some thoughts:
 * __Change the netcode to only send changes in ClientInputState.__ Right now the whole of the "input state" gets sent down the pipe, but all the server really needs is how this input is different from the last. I doubt that would be hard to actually implement, but there are more pressing issues.
 * __Switch from Slick2D to LibGDX.__ Now that I have a mostly functioning, "pure" OpenGL base of code to work with, I'd rather work with that. Slick2D is wonderful, but LibGDX gives me even more cross-platform compatability. All I need to do is add a lot of "sprite" functionality to my own codebase.
 * __Better seperate the net and game code.__ right now it's all intertwined and horrible. I'd prefer have a engine that isn't aware it's attached to a network connection. I'd prefer that the GameContainer works with a *seperate* object to communicate with the network.
 * __Gamestates.__ Always best to do them first, then add other features. Goes hand in hand with the above.
 * __Game lobby/Matchmaking.__ Goes with the above two. Deciding between P2P hosting vs centralized hosting would be a factor.
 * __Build & Run from the command line.__ This would seriously help me test a lot faster, if I could just boot a server & 2 clients straight from a single shell script.



