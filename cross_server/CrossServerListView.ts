import ComScrollView from "ComScrollView";
import Display from "Display";
import { SCROLLVIEW_DIRECTION } from "GlobalHelper";
import language from "lang";
import WindowBase from "WindowBase";
import { WinContainerTypes, WinDisposeType } from "WindowHelper";

/**
* @author gaox_xiao_si
* @description 跨服-区服列表
* 
* Date: 2021-10-21
*/
export default class CrossServerListView extends WindowBase {

    private svNode: cc.Node;
    private txt_world_lev: cc.Node;
    scroll: ComScrollView;

    zoneList;
    worldLev: number;

    constructor() {
        super();

        this.win_type = WinContainerTypes.alert;
        this.dispose_type = WinDisposeType.delay;
        this.setClickBlankClose(true);
        this.addAsyncPrefab("ui/cross_server/cross_server_list_view");
        this.addAsyncPrefab("ui/cross_server/cross_server_list_item");

    }

    public init() {
        super.init("ui/cross_server/cross_server_list_view");
        this.initUI();
        this.initFunc();
        this.addListen();

    }

    private initUI() {

        this.txt_world_lev = this.rootNode.getChildByName("txt_world_lev");
        this.svNode = this.rootNode.getChildByName("svNode");
        this.scroll = new ComScrollView({
            width: this.svNode.width,
            height: this.svNode.height,
            dir: SCROLLVIEW_DIRECTION.VERTICAL,
            itemClass: CrossServerListItem,
            itemPadding: [0, 0, 0, 0],
        });
        this.svNode.addChild(this.scroll.node);
    }

    private initFunc() {
    }

    private addListen() {
    }

    public open() {
        super.open();
        this.params = this.params || {};
        this.zoneList = this.params.list;
        this.worldLev = this.params.worldLev || 0;

        if (this.zoneList) {
            this.scroll.setData(this.zoneList);
        }

        Display.setLabelString(this.txt_world_lev, cc.js.formatStr(language.cross_server.curWorldLev, this.worldLev));
    }


    public updateView() {
    }

    public close() {
        super.close();
    }

    public dispose() {
        super.dispose()
    }
}


class CrossServerListItem {
    private readonly WIDTH: number = 620;
    private readonly HEIGHT: number = 155;

    node: cc.Node;
    data: string = "";
    txt_server_name: cc.Node;
    

    constructor() {
        this.node = Display.instantiatePrefab("ui/cross_server/cross_server_list_item");
        this.node.setContentSize(this.WIDTH, this.HEIGHT);
        this.node.setAnchorPoint(0, 0);

        this.txt_server_name = this.node.getChildByName("txt_server_name");
    }

    setData(data: string) {
        this.data = data;
        Display.setLabelString(this.txt_server_name, data);
    }
}