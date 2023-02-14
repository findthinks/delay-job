import{B as h}from"./TableImg.3ea102f7.js";import{n as g}from"./useForm.306d84e7.js";import{u as j}from"./useTable.cd6fa638.js";import{P as z}from"./index.a5c9b96c.js";import{ar as C,c as w,cp as b,a as T,at as l,o as I,k as x,B as p,p as d,G as S}from"./index.c6cd9d59.js";import{g as B,c as J}from"./job.b2f68091.js";import"./index.2e3f3fc1.js";import"./index.58195024.js";import"./warning.e82de0db.js";import"./index.40da7c35.js";import"./index.84a3ee03.js";import"./isEqual.5cd663ea.js";import"./get.1c474c66.js";import"./useWindowSizeFn.d7e3700c.js";import"./index.516d775a.js";import"./FullscreenOutlined.95b6bc70.js";import"./index.35447d01.js";import"./useSortable.c23ab760.js";import"./_baseIteratee.15e11edf.js";import"./index.1764bc9d.js";import"./index.c3e8013e.js";import"./responsiveObserve.a808bc28.js";import"./useSize.d1c02cee.js";import"./scrollTo.bc092d17.js";import"./transButton.15df72c4.js";import"./index.d37eb46e.js";import"./index.3fd27773.js";import"./download.dbdf06c7.js";import"./index.11bb4552.js";import"./index.2ea56e6e.js";import"./index.de09d623.js";import"./useBreakpoint.2016889e.js";import"./useContentViewHeight.e48256e4.js";import"./ArrowLeftOutlined.ab0ba0f8.js";const{t:e}=w(),P=(t=1)=>{const n=[];for(let a=0;a<t;a++)n.push({field:"outJobNo",label:e("biz.job.outJobNo"),component:"Input",componentProps:{maxlength:32},colProps:{xl:12,xxl:8}});return n};function _(){return{labelWidth:100,schemas:[...P(1)]}}function Y(){return[{title:e("biz.job.id"),dataIndex:"id",fixed:"left",width:100},{title:e("biz.job.outJobNo"),dataIndex:"outJobNo",width:150},{title:e("biz.job.jobShardId"),dataIndex:"jobShardId",width:150},{title:e("biz.job.triggerTime"),dataIndex:"triggerTime",width:150,format:t=>b(t,"YYYY-MM-DD HH:mm:ss")},{title:e("biz.job.gmtCreate"),dataIndex:"gmtCreate",width:150,format:t=>b(t,"YYYY-MM-DD HH:mm:ss")},{title:e("biz.job.type"),width:100,dataIndex:"type",format:t=>{switch(t){case 5:return e("biz.job.commonJob");case 10:return e("biz.job.pausableJob")}}},{title:e("biz.job.state"),width:50,dataIndex:"state",format:t=>{switch(t){case 10:return e("biz.job.submitState");case 20:return e("biz.job.retryingState");case 30:return e("biz.job.successState");case 40:return e("biz.job.faildState");case 50:return e("biz.job.canceledState")}}},{title:e("biz.job.callbackProtocol"),width:100,dataIndex:"callbackProtocol",format:t=>{switch(t){case 0:return"HTTP";case 5:return"GRPC";case 20:return"Inner Test"}}},{title:e("biz.job.params"),width:150,dataIndex:"jobInfo"}]}const H=T({components:{BasicTable:h,PageWrapper:z,TableAction:g},setup(){const{createMessage:t}=S(),{info:n,success:a,warning:u,error:f}=t,[m,{reload:i}]=j({title:e("biz.job.listTitle"),api:B,columns:Y(),pagination:!1,useSearchForm:!0,formConfig:_(),showIndexColumn:!1,actionColumn:{width:50,title:e("biz.job.option"),dataIndex:"action",slots:{customRender:"action"}}});function s(){i()}function c(){i()}function o(r){J(r).then(()=>{a(e("biz.message.success")),i()})}return{t:e,registerTable:m,handleReloadCurrent:s,handleReload:c,handleCancel:o}}});function N(t,n,a,u,f,m){const i=l("TableAction"),s=l("BasicTable"),c=l("PageWrapper");return I(),x(c,{contentBackground:"",contentClass:"flex",dense:"",contentFullHeight:"",fixedHeight:""},{default:p(()=>[d(s,{onRegister:t.registerTable},{action:p(({record:o})=>[d(i,{actions:[{label:t.t("biz.job.canceledState"),popConfirm:{title:t.t("biz.job.cancleTips"),confirm:t.handleCancel.bind(null,o)},ifShow:r=>o.state===10},{label:t.t("biz.job.pause"),popConfirm:{title:t.t("biz.job.pause"),confirm:t.handleCancel.bind(null,o)},ifShow:r=>o.state===10},{label:t.t("biz.job.recover"),popConfirm:{title:t.t("biz.job.recover"),confirm:t.handleCancel.bind(null,o)},ifShow:r=>o.state===10}]},null,8,["actions"])]),_:1},8,["onRegister"])]),_:1})}var dt=C(H,[["render",N]]);export{dt as default,P as getAdvanceSchema,Y as getBasicColumns,_ as getFormConfig};