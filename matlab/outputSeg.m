function outputSeg(L,name)

  fid = fopen([name '.seg'], 'w');
  fprintf(fid, '#(');

  [xsize,ysize]=size(L);

  for i = 1:xsize
    if i~=1 
        fprintf(fid,'   #(%g\n',L(i,1));
    else
        fprintf(fid,'#(%g\n',L(i,1));
    end
        
    for j = 2:ysize-1
      fprintf(fid,'     %g\n',L(i,j));
    end
    
    if i~=xsize
        fprintf(fid,'     %g)\n',L(i,ysize));
    else
        fprintf(fid,'     %g)',L(i,ysize));
    end
  end

  fprintf(fid,')');

  fclose(fid);

end
