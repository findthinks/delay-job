import{a as j,af as s,Y as F,f as b,W as w,N as d,p as t,d$ as Y,e4 as q,w as z,aa as D,F as G,e5 as H,b7 as J}from"./index.c6cd9d59.js";var K=j({name:"ACheckableTag",props:{prefixCls:s.string,checked:s.looseBool,onChange:{type:Function},onClick:{type:Function}},emits:["update:checked","change","click"],setup:function(e,i){var o=i.slots,r=i.emit,h=F("tag",e),u=h.prefixCls,n=function(g){var v=e.checked;r("update:checked",!v),r("change",!v),r("click",g)},k=b(function(){var l;return w(u.value,(l={},d(l,"".concat(u.value,"-checkable"),!0),d(l,"".concat(u.value,"-checkable-checked"),e.checked),l))});return function(){var l;return t("span",{class:k.value,onClick:n},[(l=o.default)===null||l===void 0?void 0:l.call(o)])}}}),p=K,L=new RegExp("^(".concat(Y.join("|"),")(-inverse)?$")),M=new RegExp("^(".concat(q.join("|"),")$")),Q={prefixCls:s.string,color:{type:String},closable:s.looseBool.def(!1),closeIcon:s.VNodeChild,visible:s.looseBool,onClose:{type:Function},icon:s.VNodeChild},C=j({name:"ATag",props:Q,emits:["update:visible","close"],slots:["closeIcon","icon"],setup:function(e,i){var o=i.slots,r=i.emit,h=i.attrs,u=F("tag",e),n=u.prefixCls,k=u.direction,l=z(!0);D(function(){e.visible!==void 0&&(l.value=e.visible)});var g=function(c){c.stopPropagation(),r("update:visible",!1),r("close",c),!c.defaultPrevented&&e.visible===void 0&&(l.value=!1)},v=b(function(){var a=e.color;return a?L.test(a)||M.test(a):!1}),R=b(function(){var a;return w(n.value,(a={},d(a,"".concat(n.value,"-").concat(e.color),v.value),d(a,"".concat(n.value,"-has-color"),e.color&&!v.value),d(a,"".concat(n.value,"-hidden"),!l.value),d(a,"".concat(n.value,"-rtl"),k.value==="rtl"),a))});return function(){var a,c,m,x=e.icon,S=x===void 0?(a=o.icon)===null||a===void 0?void 0:a.call(o):x,I=e.color,N=e.closeIcon,T=N===void 0?(c=o.closeIcon)===null||c===void 0?void 0:c.call(o):N,y=e.closable,B=y===void 0?!1:y,E=function(){return B?T?t("div",{class:"".concat(n.value,"-close-icon"),onClick:g},[T]):t(J,{class:"".concat(n.value,"-close-icon"),onClick:g},null):null},V={backgroundColor:I&&!v.value?I:void 0},P=S||null,_=(m=o.default)===null||m===void 0?void 0:m.call(o),W=P?t(G,null,[P,t("span",null,[_])]):_,A="onClick"in h,$=t("span",{class:R.value,style:V},[W,E()]);return A?t(H,null,{default:function(){return[$]}}):$}}});C.CheckableTag=p;C.install=function(f){return f.component(C.name,C),f.component(p.name,p),f};var X=C;export{X as T};