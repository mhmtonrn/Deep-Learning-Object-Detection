function net = alexnet()

breadcrumbFile = 'nnet.internal.cnn.supportpackages.IsAlexNetInstalled';
fullPath = which(breadcrumbFile);

if isempty(fullPath)
    name     = 'Neural Network Toolbox Model for AlexNet Network';
    basecode = 'ALEXNET';
    
    error(message('nnet_cnn:supportpackages:NotInstalled', mfilename, name, basecode));
else
    pattern = fullfile(filesep, '+nnet','+internal','+cnn','+supportpackages','IsAlexNetInstalled.m');
    idx     = strfind(fullPath, pattern);
    matfile = fullfile(fullPath(1:idx), 'data', 'alexnet.mat');
    data    = load(matfile);
    net     = data.alexnet;
end
