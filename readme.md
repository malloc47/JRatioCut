# JRatioCut

A partial reimplementation of the `ratio-cut` graph-cut framework

## About

While working with `ratio-cut`, I ran across a few issues when running
large-scale images. While the actual graph algorithm is very adept at
handling subimages that subsequently can be stitched back together,
there is no way (short of patching the code, which is a better
longer-term option) to handle displaying the graph in image form. Thus
I began hacking `JRatioCut`, largely to learn the underlying scheme
code, but also to display larger images.

Originally envisioned as reimplementing all of `ratio-cut`, currently
`JRatioCut` only contains the `mrc-read` component (`jmrc-read` in
`JRatioCut`). What is here is largely a learning experiment, and not
intended to replace the more robust and correct scheme version of
`ratio cut`.

The `JRead` component aims to be identical to the corresponding
`mrc-read` (even the help message is largely the same). It uses the
same arguments with the same meaning for each as `mrc-read`.

## Original

You can find the latest version of the original `ratio-cut` package
here: <http://www.cse.sc.edu/~songwang/document/ratio-cut.tar.Z>.

## License

All those components that are not part of the original ratio-cut are
Copyright 2011 Jarrell Waggoner. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are
met:

   1. Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.

   2. Redistributions in binary form must reproduce the above
      copyright notice, this list of conditions and the following
      disclaimer in the documentation and/or other materials provided
      with the distribution.

THIS SOFTWARE IS PROVIDED BY JARRELL WAGGONER ''AS IS'' AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JARRELL WAGGONER OR
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
