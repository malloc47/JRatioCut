function imgSlice(imageName, hres , vres)

img = imread(imageName);

[m,n] = size(img);

name = imageName(1,1:size(imageName,2)-4);

imwrite(img,[name '_original.pgm'],'pgm','Encoding','ASCII');

img_pre = preprocess(img);
% img_pre = img;

imwrite(img_pre,[name '.pgm'],'pgm','Encoding','ASCII');

hcounter = 1;
vcounter = 1;

hindex = 0;
vindex = 0;

while(vcounter < m )
    while(hcounter < n)
        
        image_slice = img_pre(vcounter:min(m,vcounter+vres-1),hcounter:min(n,hcounter+hres-1) );
       
        imwrite(image_slice,[name '_slice_' int2str(vindex) '_' int2str(hindex) '.pgm'],'pgm','Encoding','ASCII');
        
        hindex = hindex + 1;
        hcounter = hcounter + hres;
    end
    
    vindex = vindex + 1;
    hindex = 0;
    hcounter = 1;
    vcounter = vcounter + vres;
end

end