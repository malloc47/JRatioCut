function final = combine(prefix, hslices, vslices)

% prefix = 'csv/slice';
sep = '_';
ext = '.csv';

% m00 = dlmread('slice_0_0.csv')';
% m01 = dlmread('slice_0_1.csv')';
% m02 = dlmread('slice_0_2.csv')';
% m03 = dlmread('slice_0_3.csv')';
% m04 = dlmread('slice_0_4.csv')';
% m05 = dlmread('slice_0_5.csv')';
% m10 = dlmread('slice_1_0.csv')';
% m11 = dlmread('slice_1_1.csv')';
% m12 = dlmread('slice_1_2.csv')';
% m13 = dlmread('slice_1_3.csv')';
% m14 = dlmread('slice_1_4.csv')';
% m15 = dlmread('slice_1_5.csv')';
% m20 = dlmread('slice_2_0.csv')';
% m21 = dlmread('slice_2_1.csv')';
% m22 = dlmread('slice_2_2.csv')';
% m23 = dlmread('slice_2_3.csv')';
% m24 = dlmread('slice_2_4.csv')';
% m25 = dlmread('slice_2_5.csv')';
% m30 = dlmread('slice_3_0.csv')';
% m31 = dlmread('slice_3_1.csv')';
% m32 = dlmread('slice_3_2.csv')';
% m33 = dlmread('slice_3_3.csv')';
% m34 = dlmread('slice_3_4.csv')';
% m35 = dlmread('slice_3_5.csv')';
% m40 = dlmread('slice_4_0.csv')';
% m41 = dlmread('slice_4_1.csv')';
% m42 = dlmread('slice_4_2.csv')';
% m43 = dlmread('slice_4_3.csv')';
% m44 = dlmread('slice_4_4.csv')';
% m45 = dlmread('slice_4_5.csv')';

% final0 = [m00 m01+max(max(m00)) m02 m03 m04 m05];
% final1 = [m10 m11 m12 m13 m14 m15];
% final2 = [m20 m21 m22 m23 m24 m25];
% final3 = [m30 m31 m32 m33 m34 m35];
% final4 = [m40 m41 m42 m43 m44 m45];
% final_new = [final0; final1; final2; final3; final4];

M = cell(vslices,hslices);

max_count = uint32(0);
for j = 0:vslices-1 
    for i = 0:hslices-1
           input = dlmread([prefix sep int2str(j) sep int2str(i) ext])';
           input = uint32(input(1:size(input,1)-1,:)) + max_count;
           max_count = max(max(input))+1;
           M{j+1,i+1} = uint16(input);
   end    
end

final = [];

for j = 1:vslices
    final_row = [];
    for i = 1:hslices
        final_row = [final_row M{j,i} ];
    end
    final = [final ; final_row];
end

end
