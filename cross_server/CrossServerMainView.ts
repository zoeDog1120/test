import ActivityLimitData, { CrossServerListData } from "ActivityLimitData";
import ComScrollView from "ComScrollView";
import CrossServerManager, { class_9988 } from "CrossServerManager";
import Display from "Display";
import EventNames from "EventNames";
import { SCROLLVIEW_DIRECTION } from "GlobalHelper";
import language from "lang";
import PathDefine from "PathDefine";
import WindowBase from "WindowBase";
import { WinContainerTypes, WinDisposeType } from "WindowHelper";
import WindowManager from "WindowManager";
import WindowNames from "WindowNames";

/**
* @author gaox_xiao_si
* @description 跨服主界面
* 
* Date: 2021-10-21
*/
export default class CrossServerMainView extends WindowBase {
    private btn_back: cc.Node;

    private svNode_server: cc.Node;
    scroll_server: ComScrollView;

    svEntry: ComScrollView;

    constructor() {
        super();

        this.win_type = WinContainerTypes.normal;
        this.dispose_type = WinDisposeType.delay;
        this.addAsyncPrefab("ui/cross_server/cross_server_main_view");
        this.addAsyncPrefab("ui/cross_server/cross_server_main_view_item");
        this.addAsyncPrefab(CrossServerEntryItem.getViewLoadPrefab());

        this.addAsyncPlist("ui/cross_server/res/cross_server");
    }

    public init() {
        super.init("ui/cross_server/cross_server_main_view");
        this.initUI();
        this.initFunc();
        this.addListen();
        this.setWinBg(PathDefine.getScenePath(10017));
    }

    private initUI() {

        this.btn_back = this.rootNode.getChildByName("btn_back");
        this.svNode_server = this.rootNode.getChildByName("svNode_server");
        this.scroll_server = new ComScrollView({
            width: this.svNode_server.width,
            height: this.svNode_server.height,
            dir: SCROLLVIEW_DIRECTION.VERTICAL,
            itemClass: CrossServerItem,
            itemPadding: [0, 0, 0, 0],
        })
        this.svNode_server.addChild(this.scroll_server.node);

        //this.btn_guild_banquet_pvp = this.rootNode.getChildByName("btn_guild_banquet_pvp");

        let svNode = this.rootNode.getChildByName("svNode");
        this.svEntry = new ComScrollView({
            width: svNode.width,
            height: svNode.height,
            dir: SCROLLVIEW_DIRECTION.HORIZONTAL,
            elastic: false,
            itemClass: CrossServerEntryItem,
            itemPadding: [0, 0, 0, 10], //0,1调整Y，2，3调整X 
        })
        svNode.addChild(this.svEntry.node);
    }

    private initFunc() {
        Display.setClickEventByType(this.btn_back, this.closeBySelf, this);
    }

    private addListen() {
        this.addEvent(EventNames.CrossServer.UPDATE_GROUP, this.updateView);
    }

    public open() {
        super.open();
        // let canOpen = GuildBanquetPvpManager.getInstance().getIsOpen();
        // this.btn_guild_banquet_pvp.active = canOpen;
    }

    public openRequest() {
        super.openRequest();
        CrossServerManager.getInstance().reqCrossServer();

        // /** test */
        // this.updateView({
        //     cross_group_info: [
        //         { srv_id: 0, srv_name: "1", word_lev: 1, zone_names: ["a","b","c","d"] },
        //         { srv_id: 0, srv_name: "2", word_lev: 2, zone_names: ["e","f","g"] },
        //         { srv_id: 0, srv_name: "3", word_lev: 3, zone_names: ["h"] },
        //         { srv_id: 0, srv_name: "4", word_lev: 4, zone_names: ["i"] },
        //         { srv_id: 0, srv_name: "5", word_lev: 5, zone_names: ["j"] },
        //         { srv_id: 0, srv_name: "6", word_lev: 6, zone_names: ["k","m","n","o","p","q","r","s","t","u"] },
        //     ],
        //     next_ts: 0
        // });
        this.updateEntry();
    }


    public updateView(data: class_9988) {
        if (!data) {
            return;
        }
        let list = [];
        let posY = 0;

        // 排序
        data.cross_group_info.sort((a, b) => {
            if (a.srv_id < b.srv_id) {
                return -1;
            } else {
                return 1;
            }
        })
        // 把自己所在的服置顶
        let selfIndex: number = -1;
        for (let i = 0; i < data.cross_group_info.length; i++) {
            if (data.cross_group_info[i] && data.cross_group_info[i].zone_names) {
                let nameList = data.cross_group_info[i].zone_names;
                for (let j = 0; j < nameList.length; j++) {
                    if (nameList[j] == data.inside_zone_name) {
                        selfIndex = i;
                        break;
                    }
                }
            }
            if (selfIndex >= 0) {
                break
            }
        }
        if (selfIndex > 0) {
            let temp = data.cross_group_info.splice(selfIndex, 1);
            data.cross_group_info.unshift(temp[0]);
        }

        for (let index = 0; index < data.cross_group_info.length; index++) {
            if (!index) {
                list.push({ width: 525, height: 416, data: data.cross_group_info[index], isSelf: selfIndex >= 0 });
                posY += 416;
            } else {
                list.push({ width: 525, height: 208, data: data.cross_group_info[index], isSelf: false });
                posY += 208;
            }
        }
        this.scroll_server.setData(list, { width: 1024, height: posY });
    }

    public updateEntry() {
        let crossList = ActivityLimitData.getInstance().getCrossServerList();
        this.svEntry.setData(crossList);
    }

    public close() {
        super.close();
    }

    public dispose() {
        super.dispose()
        if (this.scroll_server) {
            this.scroll_server.dispose();
            this.scroll_server = null;
        }

        if (this.svEntry) {
            this.svEntry.dispose();
            this.svEntry = null;
        }
    }
}


class CrossServerItem {

    node: cc.Node;
    pos: cc.Node;

    btn_server: cc.Node;
    txt_server_name: cc.Node;
    txt_lev: cc.Node;
    data: {
        /** "服务器id" */
        srv_id: number;
        /** "服务器名" */
        srv_name: string;
        /** "区服名" */
        zone_names: {
            /** " 区服名" */
            zone_name: string;
            /** "自己是否在服里面 0-否 1-是" */
            self_inside: number;
        }[];
        /** "世界等级" */
        word_lev: number;
    };

    constructor() {
        this.node = Display.instantiatePrefab("ui/cross_server/cross_server_main_view_item");
        this.node.setAnchorPoint(0, 0);

        this.pos = this.node.getChildByName("pos");
        this.btn_server = this.pos.getChildByName("btn_server");
        this.txt_server_name = this.pos.getChildByName("txt_server_name");
        this.txt_lev = this.pos.getChildByName("txt_lev");

        Display.setClickEventByType(this.btn_server, () => {
            if (!this.data) {
                return;
            }
            WindowManager.getInstance().open(WindowNames.CrossServerListView, { list: this.data.zone_names, worldLev: this.data.word_lev });
        }, this);
    }


    setData(params: { posY, width, height, data, isSelf }) {
        if (!params || !params.data) {
            return;
        }
        this.data = params.data;
        if (this["sc_item_idx"] % 2) {
            this.pos.x = 525;
        } else {
            this.pos.x = 0;
        }

        Display.setLabelString(this.txt_server_name, this.data.srv_name);
        Display.setLabelString(this.txt_lev, cc.js.formatStr(language.cross_server.worldLev, this.data.word_lev));

        this.txt_server_name.color = params.isSelf ? cc.color(164, 255, 146) : cc.color(255, 255, 255);
    }
}

class CrossServerEntryItem {
    readonly width: number = 202;
    readonly height: number = 148;
    node: cc.Node;
    rootNode: cc.Node;
    img_icon: cc.Node;
    lb_name: cc.Node;
    data: CrossServerListData;
    constructor() {
        this.node = Display.newNode("CrossServerEntryItem");
        this.node.setAnchorPoint(cc.v2(0, 0));

        this.rootNode = Display.instantiatePrefab(CrossServerEntryItem.getViewLoadPrefab());
        this.node.addChild(this.rootNode)
        this.node.setContentSize(this.width, this.height);

        this.initUI();
        this.initFunc();
    }

    public static getViewLoadPrefab(): string {
        return "ui/cross_server/cross_server_main_entry_item";
    }

    private initUI() {
        let rootNode = this.rootNode;
        this.img_icon = rootNode.getChildByName("img_icon");
        this.lb_name = rootNode.getChildByName("lb_name");
    }


    private initFunc() {
        Display.setClickEventByType(this.img_icon, () => {
            if (this.data) {
                WindowManager.getInstance().checkFuncOpen(this.data.win);
            }
        }, this, { isAnim: true });
    }

    public setData(data: CrossServerListData) {
        this.data = data;
        if (!data) {
            return;
        }

        Display.setLabelString(this.lb_name, data.name);
        Display.setSpriteFrame(this.img_icon, data.icon);
    }
}