import{a as S,af as u,w as T,am as h,_ as c,p as w,X as d,b1 as b}from"./index.c6cd9d59.js";var x=globalThis&&globalThis.__rest||function(t,l){var a={};for(var e in t)Object.prototype.hasOwnProperty.call(t,e)&&l.indexOf(e)<0&&(a[e]=t[e]);if(t!=null&&typeof Object.getOwnPropertySymbols=="function")for(var o=0,e=Object.getOwnPropertySymbols(t);o<e.length;o++)l.indexOf(e[o])<0&&Object.prototype.propertyIsEnumerable.call(t,e[o])&&(a[e[o]]=t[e[o]]);return a},K={border:0,background:"transparent",padding:0,lineHeight:"inherit",display:"inline-block"},B=S({name:"TransButton",inheritAttrs:!1,props:{noStyle:u.looseBool,onClick:u.func,disabled:u.looseBool,autofocus:u.looseBool},setup:function(l,a){var e=a.slots,o=a.emit,y=a.attrs,p=a.expose,i=T(),k=function(n){var s=n.keyCode;s===b.ENTER&&n.preventDefault()},m=function(n){var s=n.keyCode;s===b.ENTER&&o("click",n)},C=function(n){o("click",n)},v=function(){i.value&&i.value.focus()},g=function(){i.value&&i.value.blur()};return h(function(){l.autofocus&&v()}),p({focus:v,blur:g}),function(){var r,n=l.noStyle,s=l.disabled,O=x(l,["noStyle","disabled"]),f={};return n||(f=c({},K)),s&&(f.pointerEvents="none"),w("div",d(d(d({role:"button",tabindex:0,ref:i},O),y),{},{onClick:C,onKeydown:k,onKeyup:m,style:c(c({},f),y.style||{})}),[(r=e.default)===null||r===void 0?void 0:r.call(e)])}}}),j=B;export{j as T};
