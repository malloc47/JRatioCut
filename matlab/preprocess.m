function img_final = preprocess(img)

% img = imread(imgName);
% A = adapthisteq(img,'clipLimit',0.02,'Distribution','uniform');
% A = imadjust(img);
% A = histeq(A);

% H = fspecial('unsharp',0.5);
% A = imfilter(A,H,'replicate');

A = medfilt2(img);
B = medfilt2(A);
C = medfilt2(B);
D = medfilt2(C);
E = medfilt2(D);


img_final = B;

end
