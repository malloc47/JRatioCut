% Use image name without extension
% Values of s should be whole integers such as 100
function L = watershedSegment(imageName,s)

I=imread([imageName '.tif']);
scalefactor = s;

hy = fspecial('sobel');
hx = hy';
Iy = imfilter(double(I/scalefactor), hy, 'replicate');
Ix = imfilter(double(I/scalefactor), hx, 'replicate');
gradmag = sqrt(Ix.^2 + Iy.^2);


L = watershed(gradmag,4);
Lrgb = label2rgb(L);

outputSeg(L,imageName);

% figure, imshow(I);
% figure, imshow(Lrgb);

end

