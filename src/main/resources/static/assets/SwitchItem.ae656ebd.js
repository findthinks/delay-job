import{ar as s,a as d,b as p,c as l,f as m,at as c,o as u,i as h,n as f,t as C,p as g,au as _,r as v}from"./index.c6cd9d59.js";import{S as y}from"./index.40da7c35.js";import{b as S}from"./index.99cb3760.js";import"./index.13dd6b06.js";import"./FullscreenOutlined.95b6bc70.js";import"./index.ff6bff65.js";import"./useWindowSizeFn.d7e3700c.js";import"./useContentViewHeight.e48256e4.js";import"./useSortable.c23ab760.js";import"./_baseIteratee.15e11edf.js";import"./get.1c474c66.js";import"./index.51422c33.js";import"./warning.e82de0db.js";import"./lock.b523afab.js";import"./ArrowLeftOutlined.ab0ba0f8.js";import"./isEqual.5cd663ea.js";import"./index.35447d01.js";const b=d({name:"SwitchItem",components:{Switch:y},props:{event:{type:Number},disabled:{type:Boolean},title:{type:String},def:{type:Boolean}},setup(e){const{prefixCls:t}=p("setting-switch-item"),{t:n}=l(),o=m(()=>e.def?{checked:e.def}:{});function a(i){e.event&&S(e.event,i)}return{prefixCls:t,t:n,handleChange:a,getBindValue:o}}});function k(e,t,n,o,a,i){const r=c("Switch");return u(),h("div",{class:v(e.prefixCls)},[f("span",null,C(e.title),1),g(r,_(e.getBindValue,{onChange:e.handleChange,disabled:e.disabled,checkedChildren:e.t("layout.setting.on"),unCheckedChildren:e.t("layout.setting.off")}),null,16,["onChange","disabled","checkedChildren","unCheckedChildren"])],2)}var K=s(b,[["render",k],["__scopeId","data-v-fd7354e2"]]);export{K as default};