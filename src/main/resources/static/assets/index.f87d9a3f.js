import{af as r,ae as Y,a as Z,a7 as tt,T as at,aB as et,cg as G,dE as nt,p as t,cy as ot,b3 as rt,b6 as f,N as l}from"./index.c6cd9d59.js";import{T as R}from"./index.51422c33.js";import{R as v,C as s}from"./index.11bb4552.js";var lt=R.TabPane,it={prefixCls:r.string,title:r.VNodeChild,extra:r.VNodeChild,bordered:r.looseBool.def(!0),bodyStyle:r.style,headStyle:r.style,loading:r.looseBool.def(!1),hoverable:r.looseBool.def(!1),type:r.string,size:r.oneOf(Y("default","small")),actions:r.VNodeChild,tabList:{type:Array},tabBarExtraContent:r.VNodeChild,activeTabKey:r.string,defaultActiveTabKey:r.string,cover:r.VNodeChild,onTabChange:{type:Function}},st=Z({name:"ACard",mixins:[tt],props:it,setup:function(){return{configProvider:at("configProvider",et)}},data:function(){return{widerPadding:!1}},methods:{getAction:function(n){var i=n.map(function(e,g){return G(e)&&!nt(e)||!G(e)?t("li",{style:{width:"".concat(100/n.length,"%")},key:"action-".concat(g)},[t("span",null,[e])]):null});return i},triggerTabChange:function(n){this.$emit("tabChange",n)},isContainGrid:function(){var n=arguments.length>0&&arguments[0]!==void 0?arguments[0]:[],i;return n.forEach(function(e){e&&ot(e.type)&&e.type.__ANT_CARD_GRID&&(i=!0)}),i}},render:function(){var n,i,e=this.$props,g=e.prefixCls,m=e.headStyle,w=m===void 0?{}:m,x=e.bodyStyle,b=x===void 0?{}:x,$=e.loading,T=e.bordered,L=T===void 0?!0:T,P=e.size,k=P===void 0?"default":P,A=e.type,c=e.tabList,j=e.hoverable,S=e.activeTabKey,O=e.defaultActiveTabKey,B=this.$slots,p=rt(this),F=this.configProvider.getPrefixCls,a=F("card",g),I=f(this,"tabBarExtraContent"),M=(n={},l(n,"".concat(a),!0),l(n,"".concat(a,"-loading"),$),l(n,"".concat(a,"-bordered"),L),l(n,"".concat(a,"-hoverable"),!!j),l(n,"".concat(a,"-contain-grid"),this.isContainGrid(p)),l(n,"".concat(a,"-contain-tabs"),c&&c.length),l(n,"".concat(a,"-").concat(k),k!=="default"),l(n,"".concat(a,"-type-").concat(A),!!A),n),q=b.padding===0||b.padding==="0px"?{padding:24}:void 0,H=t("div",{class:"".concat(a,"-loading-content"),style:q},[t(v,{gutter:8},{default:function(){return[t(s,{span:22},{default:function(){return[t("div",{class:"".concat(a,"-loading-block")},null)]}})]}}),t(v,{gutter:8},{default:function(){return[t(s,{span:8},{default:function(){return[t("div",{class:"".concat(a,"-loading-block")},null)]}}),t(s,{span:15},{default:function(){return[t("div",{class:"".concat(a,"-loading-block")},null)]}})]}}),t(v,{gutter:8},{default:function(){return[t(s,{span:6},{default:function(){return[t("div",{class:"".concat(a,"-loading-block")},null)]}}),t(s,{span:18},{default:function(){return[t("div",{class:"".concat(a,"-loading-block")},null)]}})]}}),t(v,{gutter:8},{default:function(){return[t(s,{span:13},{default:function(){return[t("div",{class:"".concat(a,"-loading-block")},null)]}}),t(s,{span:9},{default:function(){return[t("div",{class:"".concat(a,"-loading-block")},null)]}})]}}),t(v,{gutter:8},{default:function(){return[t(s,{span:4},{default:function(){return[t("div",{class:"".concat(a,"-loading-block")},null)]}}),t(s,{span:3},{default:function(){return[t("div",{class:"".concat(a,"-loading-block")},null)]}}),t(s,{span:16},{default:function(){return[t("div",{class:"".concat(a,"-loading-block")},null)]}})]}})]),K=S!==void 0,J=(i={size:"large"},l(i,K?"activeKey":"defaultActiveKey",K?S:O),l(i,"tabBarExtraContent",I),l(i,"onChange",this.triggerTabChange),l(i,"class","".concat(a,"-head-tabs")),i),N,E=c&&c.length?t(R,J,{default:function(){return[c.map(function(o){var z=o.tab,_=o.slots,D=_==null?void 0:_.tab,X=z!==void 0?z:B[D]?B[D](o):null;return t(lt,{tab:X,key:o.key,disabled:o.disabled},null)})]}}):null,h=f(this,"title"),y=f(this,"extra");(h||y||E)&&(N=t("div",{class:"".concat(a,"-head"),style:w},[t("div",{class:"".concat(a,"-head-wrapper")},[h&&t("div",{class:"".concat(a,"-head-title")},[h]),y&&t("div",{class:"".concat(a,"-extra")},[y])]),E]));var V=f(this,"cover"),Q=V?t("div",{class:"".concat(a,"-cover")},[V]):null,U=t("div",{class:"".concat(a,"-body"),style:b},[$?H:p]),C=f(this,"actions"),W=C&&C.length?t("ul",{class:"".concat(a,"-actions")},[this.getAction(C)]):null;return t("div",{class:M,ref:"cardContainerRef"},[N,Q,p?U:null,W])}}),ft=st;export{ft as C};