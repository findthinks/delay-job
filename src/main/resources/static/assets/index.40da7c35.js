import{b5 as M,ae as O,af as a,a as I,bA as K,al as m,w as y,f as w,Z as p,Y as A,am as P,ag as j,N as i,p as h,X as k,b9 as z,a6 as U,dd as g,e5 as D,b1 as S}from"./index.c6cd9d59.js";var E=O("small","default"),H={prefixCls:a.string,size:a.oneOf(E),disabled:a.looseBool,checkedChildren:a.VNodeChild,unCheckedChildren:a.VNodeChild,tabindex:a.oneOfType([a.string,a.number]),autofocus:a.looseBool,loading:a.looseBool,checked:a.oneOfType([a.string,a.number,a.looseBool]),checkedValue:a.oneOfType([a.string,a.number,a.looseBool]).def(!0),unCheckedValue:a.oneOfType([a.string,a.number,a.looseBool]).def(!1),onChange:{type:Function},onClick:{type:Function},onKeydown:{type:Function},onMouseup:{type:Function},"onUpdate:checked":{type:Function}},L=I({name:"ASwitch",__ANT_SWITCH:!0,inheritAttrs:!1,props:H,slots:["checkedChildren","unCheckedChildren"],emits:["update:checked","mouseup","change","click","keydown"],setup:function(e,s){var d=s.attrs,C=s.slots,V=s.expose,o=s.emit;K(function(){m(!("defaultChecked"in d),"Switch","'defaultChecked' is deprecated, please use 'v-model:checked'"),m(!("value"in d),"Switch","`value` is not validate prop, do you mean `checked`?")});var r=y(e.checked!==void 0?e.checked:d.defaultChecked),f=w(function(){return r.value===e.checkedValue});p(function(){return e.checked},function(){r.value=e.checked});var x=A("switch",e),u=x.prefixCls,t=y(),b=function(){var n;(n=t.value)===null||n===void 0||n.focus()},T=function(){var n;(n=t.value)===null||n===void 0||n.blur()};V({focus:b,blur:T}),P(function(){j(function(){e.autofocus&&!e.disabled&&t.value.focus()})});var v=function(n,l){e.disabled||(o("update:checked",n),o("change",n,l))},N=function(n){b();var l=f.value?e.unCheckedValue:e.checkedValue;v(l,n),o("click",l,n)},_=function(n){n.keyCode===S.LEFT?v(e.unCheckedValue,n):n.keyCode===S.RIGHT&&v(e.checkedValue,n),o("keydown",n)},B=function(n){var l;(l=t.value)===null||l===void 0||l.blur(),o("mouseup",n)},F=w(function(){var c;return c={},i(c,"".concat(u.value,"-small"),e.size==="small"),i(c,"".concat(u.value,"-loading"),e.loading),i(c,"".concat(u.value,"-checked"),f.value),i(c,"".concat(u.value,"-disabled"),e.disabled),i(c,u.value,!0),c});return function(){return h(D,{insertExtraNode:!0},{default:function(){return[h("button",k(k(k({},z(e,["prefixCls","checkedChildren","unCheckedChildren","checked","autofocus","defaultChecked","checkedValue","unCheckedValue"])),d),{},{onKeydown:_,onClick:N,onMouseup:B,type:"button",role:"switch","aria-checked":r.value,disabled:e.disabled||e.loading,class:[d.class,F.value],ref:t}),[e.loading?h(U,{class:"".concat(u.value,"-loading-icon")},null):null,h("span",{class:"".concat(u.value,"-inner")},[f.value?g(C,e,"checkedChildren"):g(C,e,"unCheckedChildren")])])]}})}}}),R=M(L);export{R as S};
