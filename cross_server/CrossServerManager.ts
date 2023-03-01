import CommonBus from "CommonBus";
import EventNames from "EventNames";
import NetworkManager from "NetworkManager";

export default class CrossServerManager {

    crossServerInfo: class_9988;

    private _isInCrossServer: boolean = false;

    private static _instance: CrossServerManager;
    public static getInstance(): CrossServerManager {
        if (!CrossServerManager._instance) {
            CrossServerManager._instance = new CrossServerManager();
        }
        return CrossServerManager._instance
    }


    private constructor() {
        this.addListens();
    }
    private addListens(): void {
        NetworkManager.getInstance().addHandler(9988, this.on9988, this);

    }

    public reqCrossServer() {
        NetworkManager.getInstance().send(9988, {});
    }
    private on9988(u9988: class_9988) {
        this.crossServerInfo = u9988;
        this._isInCrossServer = false;
        for (let i = 0; i < this.crossServerInfo.cross_group_info.length; i++) {
            let info = this.crossServerInfo.cross_group_info[i];
            for (let j = 0; j < info.zone_names.length; j++) {
                let zoneName = info.zone_names[j];
                if (zoneName == u9988.inside_zone_name) {
                    this._isInCrossServer = true;
                    break;
                }
            }
            if (this._isInCrossServer) {
                break;
            }
        }
        CommonBus.dispatchEvent(EventNames.CrossServer.UPDATE_GROUP, u9988);
    }


    public get isInCrossServer(): boolean {
        return this._isInCrossServer;
    }

}

export interface class_9988 {
    /** "跨服分组信息" */
    cross_group_info: {
        /** "服务器id" */
        srv_id: number;
        /** "服务器名" */
        srv_name: string;
        /** "区服名" */
        zone_names: string[];
        /** "世界等级" */
        word_lev: number;
    }[]
    /** "下次重置时间戳" */
    next_ts: number;
    /** 自己所在的区服名 */
    inside_zone_name: string;
}