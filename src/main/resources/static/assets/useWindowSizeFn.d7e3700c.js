import{cf as o,aq as a,bE as d}from"./index.c6cd9d59.js";function m(r,s=150,n){let e=()=>{r()};e=d(e,s);const t=()=>{n&&n.immediate&&e(),window.addEventListener("resize",e)},i=()=>{window.removeEventListener("resize",e)};return o(()=>{t()}),a(()=>{i()}),[t,i]}export{m as u};