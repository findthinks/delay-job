var w=(h,m,t)=>new Promise((e,p)=>{var u=r=>{try{a(t.next(r))}catch(s){p(s)}},f=r=>{try{a(t.throw(r))}catch(s){p(s)}},a=r=>r.done?e(r.value):Promise.resolve(r.value).then(u,f);a((t=t.apply(h,m)).next())});import{d_ as C,a as k,w as E,c as D,e9 as T,Z as R,ag as A,a9 as B,at as I,o as g,i as _,F as S,aw as V,y as j,z as M,p as i,j as o,B as n,D as y,t as b}from"./index.c6cd9d59.js";import F from"./DetailModal.ff14cb00.js";import{B as N}from"./TableImg.3ea102f7.js";import{n as $}from"./useForm.306d84e7.js";import{u as z}from"./useTable.cd6fa638.js";import{b as H}from"./index.516d775a.js";import{getColumns as Z}from"./data.9ea5d215.js";import"./index.b72cc388.js";import"./responsiveObserve.a808bc28.js";import"./get.1c474c66.js";import"./index.2e3f3fc1.js";import"./index.a5c9b96c.js";import"./index.2ea56e6e.js";import"./index.de09d623.js";import"./useBreakpoint.2016889e.js";import"./useSize.d1c02cee.js";import"./useWindowSizeFn.d7e3700c.js";import"./useContentViewHeight.e48256e4.js";import"./transButton.15df72c4.js";import"./ArrowLeftOutlined.ab0ba0f8.js";import"./index.58195024.js";import"./warning.e82de0db.js";import"./index.40da7c35.js";import"./index.84a3ee03.js";import"./isEqual.5cd663ea.js";import"./index.35447d01.js";import"./useSortable.c23ab760.js";import"./_baseIteratee.15e11edf.js";import"./FullscreenOutlined.95b6bc70.js";import"./index.1764bc9d.js";import"./index.c3e8013e.js";import"./scrollTo.bc092d17.js";import"./index.d37eb46e.js";import"./index.3fd27773.js";import"./download.dbdf06c7.js";import"./index.11bb4552.js";const q=()=>C.get({url:"/error"}),G={class:"p-4"},J=["src"],Ar=k({__name:"index",setup(h){const m=E(),t=E([]),{t:e}=D(),p=T(),[u,{setTableData:f}]=z({title:e("sys.errorLog.tableTitle"),columns:Z(),actionColumn:{width:80,title:"Action",dataIndex:"action",slots:{customRender:"action"}}}),[a,{openModal:r}]=H();R(()=>p.getErrorLogInfoList,l=>{A(()=>{f(B(l))})},{immediate:!0});function s(l){m.value=l,r(!0)}function L(){throw new Error("fire vue error!")}function v(){t.value.push(`${new Date().getTime()}.png`)}function x(){return w(this,null,function*(){yield q()})}return(l,K)=>{const d=I("a-button");return g(),_("div",G,[(g(!0),_(S,null,V(t.value,c=>j((g(),_("img",{key:c,src:c},null,8,J)),[[M,!1]])),128)),i(F,{info:m.value,onRegister:o(a)},null,8,["info","onRegister"]),i(o(N),{onRegister:o(u),class:"error-handle-table"},{toolbar:n(()=>[i(d,{onClick:L,type:"primary"},{default:n(()=>[y(b(o(e)("sys.errorLog.fireVueError")),1)]),_:1}),i(d,{onClick:v,type:"primary"},{default:n(()=>[y(b(o(e)("sys.errorLog.fireResourceError")),1)]),_:1}),i(d,{onClick:x,type:"primary"},{default:n(()=>[y(b(o(e)("sys.errorLog.fireAjaxError")),1)]),_:1})]),action:n(({record:c})=>[i(o($),{actions:[{label:o(e)("sys.errorLog.tableActionDesc"),onClick:s.bind(null,c)}]},null,8,["actions"])]),_:1},8,["onRegister"])])}}});export{Ar as default};