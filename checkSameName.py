import json
import os
import shutil

# 重名信息输出文本路径，一般是桌面
inputTxtPath = "H:/star2/sameImgNameJson.json";
# 检查重名路径
#checkPath = os.getcwd() + "/tps"
checkPath = "H:/star2/star2_client/tps"

f = open(inputTxtPath, "w");

fileNameDic = {};
for cur_path, cur_dirs, cur_files_name in os.walk(checkPath):
    for oneFileName in cur_files_name:
        if oneFileName not in fileNameDic:
            fileNameDic[oneFileName] = [];
        oneFilePath = os.path.join(cur_path, oneFileName).replace("\\", "/");
        fileNameDic[oneFileName].append(oneFilePath);

repeatDic = {};
for oneFileNameDicKey in fileNameDic:
    if len(fileNameDic[oneFileNameDicKey]) >= 2:
        print(fileNameDic[oneFileNameDicKey])
        repeatDic[oneFileNameDicKey] = fileNameDic[oneFileNameDicKey];
print("yyy",os.getcwd(),os.path)

# 打印未使用到的资源
file = open(inputTxtPath, 'w')
file.write(json.dumps(repeatDic))
