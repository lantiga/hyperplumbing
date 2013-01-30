# hyperplumbing

A playground for use of [Prismatic](https://github.com/prismatic)'s [plumbing](https://github.com/Prismatic/plumbing.git) library (aka [Graph](http://blog.getprismatic.com/blog/2012/10/1/prismatics-graph-at-strange-loop.html)) in distributed environments based on [exoref](https://github.com/lantiga/exoref) (a Redis-based reference types implementation).

## Details

This is work in progress, not intended for use in production.

This project is intended to demonstrate a possible design for distributed Graph based on Redis, in which different computing nodes reside in different processes and computations are triggered by consumers through lazy evaluation.  The system should be resilient with respect to the failure of individual processes (or Redis itself).

## Contact

For questions please contact me at luca dot antiga at [orobix](http://www.orobix.com) dot com. Pull requests are greatly welcome.

## License

Copyright © 2013 Luca Antiga.

Distributed under the Eclipse Public License, the same as Clojure.
