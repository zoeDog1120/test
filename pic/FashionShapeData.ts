import DataManager from "DataManager"
import Utils from "Utils"
import PathDefine from "PathDefine"
import { LooksType } from "GlobalHelper"
import TextureManager from "TextureManager"
import FashionData from "FashionData"

export class FashionShapeData {
    private static _data = null
    private static _fashionBaseDic = null
    private static _fashionSuitIds = null
    private static _fashionSuitDic = null
    private static _fashion_shape_offset_data = null
    private static _zhuangxiu_shape_offset_data = null
    private static _zhuangxiu_shape_size_data = null
    private static _fashion_shape_eye_data = null
    private static _fashion_shape_pose_data = null

    static init() {
        if (FashionShapeData._data) {
            return
        }
        FashionShapeData._data = DataManager.getInstance().get("fashion_shape_fashion_base")
        FashionShapeData._fashionBaseDic = FashionShapeData._data
        FashionShapeData._fashionSuitDic = {}
        FashionShapeData._fashionSuitIds = {}
        for (const key in FashionShapeData._data) {
            if (Object.prototype.hasOwnProperty.call(FashionShapeData._data, key)) {
                const v = FashionShapeData._data[key];
                FashionShapeData._fashionSuitDic[v.resId] = FashionShapeData._fashionSuitDic[v.resId] || []
                FashionShapeData._fashionSuitDic[v.resId].push(v)
                FashionShapeData._fashionSuitIds[v.sex] = FashionShapeData._fashionSuitIds[v.sex] || {}
                FashionShapeData._fashionSuitIds[v.sex][v.resId] = true
            }
        }

        FashionShapeData._fashion_shape_offset_data = {}
        let path = "shape/fashion_shape_offset"
        let asset = cc.resources.get(path, cc.JsonAsset) as cc.JsonAsset;
        if (asset && asset.json) {
            let list = asset.json.fasion_shape_offset_data
            for (let i = 0; i < list.length; i += 3 ) {
                FashionShapeData._fashion_shape_offset_data[PathDefine.SHAPE + list[i]] =
                cc.v2(list[i+1],list[i+2])
            }
        } else {
            // console.log("==========read file error:" + path)
        }

        FashionShapeData._zhuangxiu_shape_offset_data = {}
        FashionShapeData._zhuangxiu_shape_size_data = {}
        path = "shape/zhuangxiu_shape_offset"
        asset = cc.resources.get(path, cc.JsonAsset) as cc.JsonAsset;
        if (asset && asset.json) {
            let list = asset.json.zhuangxiu_shape_offset_data
            for (let i = 0; i < list.length; i += 5 ) {
                FashionShapeData._zhuangxiu_shape_offset_data[PathDefine.SHAPE + list[i]] =
                cc.v2(list[i+1],list[i+2])
                FashionShapeData._zhuangxiu_shape_size_data[PathDefine.SHAPE + list[i]] =
                cc.v2(list[i+3],list[i+4])
            }
        } else {
            // console.log("==========read file error:" + path)
        }

        let configData = DataManager.getInstance().get("fashion_shape_fashion_eye")
        FashionShapeData._fashion_shape_eye_data = {}
        configData.forEach(v => {
            FashionShapeData._fashion_shape_eye_data[v.id] = true
        })

        configData = DataManager.getInstance().get("fashion_shape_fashion_pose")
        FashionShapeData._fashion_shape_pose_data = {}
        configData.forEach(v => {
            FashionShapeData._fashion_shape_pose_data[v.pose] = v
        })
    }
    
    static getFashionListByType(fashionType: any, sex: any) {
        this.init()
        let ret = []
        if (! fashionType) {
            return ret
        }

        FashionShapeData._fashionBaseDic.forEach(v => {
            if (v.type == fashionType && v.sex == sex) {
                ret.push({fashionId:v.id})
            }
        });
        return ret
    }

    static getRoleFashionShapeData(fashionId: any) {
        this.init()
        if (! fashionId) {
            return null
        }
        let ret = FashionShapeData._fashionBaseDic[fashionId]
        if (ret && typeof(ret.pieceInfo) == "string") {
            let tempInfo = Utils.str2arr(ret.pieceInfo)
            ret.pieceInfo = []
            tempInfo.forEach(v => {
                let zorder
		        // 超过1个zorder,说明该片时装对人物不同的动作有不同的显示
                if (v.length > 2) {
                    zorder = {}
                    for (let i = 0; i < v.length-1; i++) {
                        zorder[i] = Utils.any2num(v[i+1])
                    }
                } else {
                    zorder = Utils.any2num(v[1])
                }
                ret.pieceInfo.push({
                    pieceId : Utils.any2num(v[0]),
                    zorder : zorder,
                })
            })
            
            tempInfo = Utils.str2arr(ret.effect_pieceInfo)
            ret.effect_pieceInfo = []
            tempInfo.forEach(v => {
                let zorder
		        // 超过1个zorder,说明该片时装对人物不同的动作有不同的显示
                if (v.length > 2) {
                    zorder = {}
                    for (let i = 0; i < v.length-1; i++) {
                        zorder[i] = Utils.any2num(v[i+1])
                    }
                } else {
                    zorder = Utils.any2num(v[1])
                }
                ret.effect_pieceInfo.push({
                    pieceId : Utils.any2num(v[0]),
                    zorder : zorder,
                })
            })

            if (ret.extLook != "") {
                tempInfo = Utils.str2arr(ret.extLook)
                ret.extLook = []
                tempInfo.forEach(v => {
                    let fid = Utils.any2num(v[0])
                    let extFashionData = FashionShapeData._fashionBaseDic[fid]
                    if (extFashionData) {
                        ret.extLook.push({
                            fashionId : fid,
                            ftype : extFashionData.type,
                        })
                    }
                })
            } else {
                ret.extLook = null
            }
        }
        return ret
    }

    static getPos(str: any) {
        if (! str || str == "" || str == "null") {
            return null
        }
        let tbl = Utils.split(str,",")
        return cc.v2(Utils.any2num(tbl[0]),Utils.any2num(tbl[1]))
    }

    static getOffset(path: any) {
        this.init()
        return FashionShapeData._fashion_shape_offset_data[path] || cc.v2(0,0)
    }

    static getZhuangxiuOffset(path: any):cc.Vec2{
        this.init()
        return FashionShapeData._zhuangxiu_shape_offset_data[path] || cc.v2(0,0)
    }

    static getZhuangxiuSize(path: any):cc.Vec2{
        this.init()
        return FashionShapeData._zhuangxiu_shape_size_data[path] || cc.v2(0,0)
    }

    static getAllSuitBySex(sex: any) {
        this.init()
        return FashionShapeData._fashionSuitIds[sex]
    }

    static getSuitList(suitId: any) {
        this.init()
        return FashionShapeData._fashionSuitDic[suitId] || []
    }

    static isFashionEyeNeedHigh(fashionId: any) {
        FashionShapeData.init()
        return FashionShapeData._fashion_shape_eye_data[fashionId]
    }

    static getPoseDataById(pId: any) {
        FashionShapeData.init()
        let tmpData = FashionShapeData._fashion_shape_pose_data[pId]
        if (tmpData && typeof(tmpData.clothes_need) == "string") {
            let tmpInfo = Utils.str2arr(tmpData.clothes_need)
            let cNeed = {}
            tmpData.clothes_need_num = tmpInfo.length
            tmpInfo.forEach(v => {
                if (v[0] == LooksType.JEWELRY) {
                    let rawData = FashionData.getInstance().getFashionRawData(v[1])
                    if (rawData && rawData.fitting_type > 0) {
                        cNeed[rawData.fitting_type] = v[1]
                        } else {
                        cNeed[v[0]] = v[1]
                    }
                    } else {
                    cNeed[v[0]] = v[1]
                }
            })
            tmpData.clothes_need = cNeed
            tmpData.clothes_pose = Utils.str2arr(tmpData.clothes_pose)
            tmpInfo = Utils.str2arr(tmpData.skin)
            cNeed = {}
            tmpInfo.forEach(v => {
                cNeed[v[0]] = v[1]
            })
            tmpData.skin = cNeed
        }
        return tmpData
    }

}
