import{aC as H,aD as K,aE as D,aF as q,aG as G,aH as J,aI as I,aJ as M,aK as $,aL as x,aM as E,aN as Q,aO as B,aP as X}from"./index.c6cd9d59.js";function Y(n,e){for(var f=-1,a=n==null?0:n.length;++f<a;)if(e(n[f],f,n))return!0;return!1}var Z=1,W=2;function F(n,e,f,a,g,r){var s=f&Z,u=n.length,l=e.length;if(u!=l&&!(s&&l>u))return!1;var v=r.get(n),O=r.get(e);if(v&&O)return v==e&&O==n;var A=-1,i=!0,p=f&W?new H:void 0;for(r.set(n,e),r.set(e,n);++A<u;){var d=n[A],T=e[A];if(a)var P=s?a(T,d,A,e,n,r):a(d,T,A,n,e,r);if(P!==void 0){if(P)continue;i=!1;break}if(p){if(!Y(e,function(t,L){if(!K(p,L)&&(d===t||g(d,t,f,a,r)))return p.push(L)})){i=!1;break}}else if(!(d===T||g(d,T,f,a,r))){i=!1;break}}return r.delete(n),r.delete(e),i}function m(n){var e=-1,f=Array(n.size);return n.forEach(function(a,g){f[++e]=[g,a]}),f}var z=1,c=2,j="[object Boolean]",V="[object Date]",h="[object Error]",o="[object Map]",k="[object Number]",nn="[object RegExp]",en="[object Set]",rn="[object String]",an="[object Symbol]",fn="[object ArrayBuffer]",sn="[object DataView]",N=D?D.prototype:void 0,R=N?N.valueOf:void 0;function gn(n,e,f,a,g,r,s){switch(f){case sn:if(n.byteLength!=e.byteLength||n.byteOffset!=e.byteOffset)return!1;n=n.buffer,e=e.buffer;case fn:return!(n.byteLength!=e.byteLength||!r(new G(n),new G(e)));case j:case V:case k:return q(+n,+e);case h:return n.name==e.name&&n.message==e.message;case nn:case rn:return n==e+"";case o:var u=m;case en:var l=a&z;if(u||(u=J),n.size!=e.size&&!l)return!1;var v=s.get(n);if(v)return v==e;a|=c,s.set(n,e);var O=F(u(n),u(e),a,g,r,s);return s.delete(n),O;case an:if(R)return R.call(n)==R.call(e)}return!1}var ln=1,un=Object.prototype,vn=un.hasOwnProperty;function An(n,e,f,a,g,r){var s=f&ln,u=I(n),l=u.length,v=I(e),O=v.length;if(l!=O&&!s)return!1;for(var A=l;A--;){var i=u[A];if(!(s?i in e:vn.call(e,i)))return!1}var p=r.get(n),d=r.get(e);if(p&&d)return p==e&&d==n;var T=!0;r.set(n,e),r.set(e,n);for(var P=s;++A<l;){i=u[A];var t=n[i],L=e[i];if(a)var S=s?a(L,t,i,e,n,r):a(t,L,i,n,e,r);if(!(S===void 0?t===L||g(t,L,f,a,r):S)){T=!1;break}P||(P=i=="constructor")}if(T&&!P){var _=n.constructor,w=e.constructor;_!=w&&"constructor"in n&&"constructor"in e&&!(typeof _=="function"&&_ instanceof _&&typeof w=="function"&&w instanceof w)&&(T=!1)}return r.delete(n),r.delete(e),T}var dn=1,C="[object Arguments]",U="[object Array]",y="[object Object]",Tn=Object.prototype,b=Tn.hasOwnProperty;function On(n,e,f,a,g,r){var s=M(n),u=M(e),l=s?U:$(n),v=u?U:$(e);l=l==C?y:l,v=v==C?y:v;var O=l==y,A=v==y,i=l==v;if(i&&x(n)){if(!x(e))return!1;s=!0,O=!1}if(i&&!O)return r||(r=new E),s||Q(n)?F(n,e,f,a,g,r):gn(n,e,l,f,a,g,r);if(!(f&dn)){var p=O&&b.call(n,"__wrapped__"),d=A&&b.call(e,"__wrapped__");if(p||d){var T=p?n.value():n,P=d?e.value():e;return r||(r=new E),g(T,P,f,a,r)}}return i?(r||(r=new E),An(n,e,f,a,g,r)):!1}function pn(n,e,f,a,g){return n===e?!0:n==null||e==null||!B(n)&&!B(e)?n!==n&&e!==e:On(n,e,f,a,pn,g)}function tn(n,e,f){var a=n==null?void 0:X(n,e);return a===void 0?f:a}export{pn as b,tn as g};
