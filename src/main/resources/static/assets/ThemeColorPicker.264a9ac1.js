import{ar as m,a as l,cX as c,b as d,at as u,o as r,i as o,F as C,aw as _,r as a,bx as f,p as k}from"./index.c6cd9d59.js";import{b as h}from"./index.99cb3760.js";import"./index.13dd6b06.js";import"./FullscreenOutlined.95b6bc70.js";import"./index.ff6bff65.js";import"./useWindowSizeFn.d7e3700c.js";import"./useContentViewHeight.e48256e4.js";import"./useSortable.c23ab760.js";import"./_baseIteratee.15e11edf.js";import"./get.1c474c66.js";import"./index.51422c33.js";import"./warning.e82de0db.js";import"./lock.b523afab.js";import"./ArrowLeftOutlined.ab0ba0f8.js";import"./isEqual.5cd663ea.js";import"./index.35447d01.js";const v=l({name:"ThemeColorPicker",components:{CheckOutlined:c},props:{colorList:{type:Array,defualt:[]},event:{type:Number},def:{type:String}},setup(e){const{prefixCls:i}=d("setting-theme-picker");function n(s){e.event&&h(e.event,s)}return{prefixCls:i,handleClick:n}}}),y=["onClick"];function $(e,i,n,s,b,g){const p=u("CheckOutlined");return r(),o("div",{class:a(e.prefixCls)},[(r(!0),o(C,null,_(e.colorList||[],t=>(r(),o("span",{key:t,onClick:x=>e.handleClick(t),class:a([`${e.prefixCls}__item`,{[`${e.prefixCls}__item--active`]:e.def===t}]),style:f({background:t})},[k(p)],14,y))),128))],2)}var j=m(v,[["render",$]]);export{j as default};