import{B as p}from"./TableImg.3ea102f7.js";import"./useForm.306d84e7.js";import{u as m}from"./useTable.cd6fa638.js";import{P as n}from"./index.a5c9b96c.js";import{ar as d,c as l,cp as c,a as u,at as i,o as f,k as h,B as b,p as x}from"./index.c6cd9d59.js";import{s as T}from"./scheduler.8663a6e7.js";import"./index.2e3f3fc1.js";import"./index.58195024.js";import"./warning.e82de0db.js";import"./index.40da7c35.js";import"./index.84a3ee03.js";import"./isEqual.5cd663ea.js";import"./get.1c474c66.js";import"./useWindowSizeFn.d7e3700c.js";import"./index.516d775a.js";import"./FullscreenOutlined.95b6bc70.js";import"./index.35447d01.js";import"./useSortable.c23ab760.js";import"./_baseIteratee.15e11edf.js";import"./index.1764bc9d.js";import"./index.c3e8013e.js";import"./responsiveObserve.a808bc28.js";import"./useSize.d1c02cee.js";import"./scrollTo.bc092d17.js";import"./transButton.15df72c4.js";import"./index.d37eb46e.js";import"./index.3fd27773.js";import"./download.dbdf06c7.js";import"./index.11bb4552.js";import"./index.2ea56e6e.js";import"./index.de09d623.js";import"./useBreakpoint.2016889e.js";import"./useContentViewHeight.e48256e4.js";import"./ArrowLeftOutlined.ab0ba0f8.js";const{t:e}=l(),_=[{title:e("biz.scheduler.id"),dataIndex:"id",width:50},{title:e("biz.scheduler.jobShardIds"),dataIndex:"jobShardIds",width:100,format:t=>t?t.join(","):"-"},{title:e("biz.scheduler.lastHeartbeatTime"),dataIndex:"lastHeartbeatTime",width:100,format:t=>c(t,"YYYY-MM-DD HH:mm:ss.SSS")},{title:e("biz.scheduler.ip"),width:100,dataIndex:"ip"},{title:e("biz.scheduler.nodeType"),width:50,dataIndex:"master",format:(t,r,o)=>o===0?"Master":"Worker"}],g=u({components:{BasicTable:p,PageWrapper:n},setup(){const[t,{reload:r}]=m({title:e("biz.scheduler.listTitle"),api:T,columns:_,pagination:!1,showIndexColumn:!1});return{registerTable:t}}});function B(t,r,o,I,w,z){const a=i("BasicTable"),s=i("PageWrapper");return f(),h(s,{contentBackground:"",contentClass:"flex",dense:"",contentFullHeight:"",fixedHeight:""},{default:b(()=>[x(a,{onRegister:t.registerTable},null,8,["onRegister"])]),_:1})}var at=d(g,[["render",B]]);export{at as default};
