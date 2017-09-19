#extension GL_OES_EGL_image_external : require

varying highp vec2 textureCoordinate;
varying highp vec2 textureCoordinate2;
varying highp vec2 textureCoordinate3;

uniform sampler2D inputImageTexture;
uniform sampler2D inputImageTexture2;
uniform sampler2D inputImageTexture3;
uniform mediump float smoothDegree;

void main(){
highp vec4 bilateral = texture2D(inputImageTexture,textureCoordinate);
highp vec4 canny = texture2D(inputImageTexture2,textureCoordinate2);
highp vec4 origin = texture2D(inputImageTexture3,textureCoordinate3);
highp vec4 smooth;
lowp float r = origin.r;
lowp float g = origin.g;
lowp float b = origin.b;
if(canny.r<0.2 && r> 0.3725 && g> 0.1568 && b>0.0784 && r>b
&&(max(max(r,g),b)-min(min(r,g),b))>0.0588 && abs(r-g)>0.0588){
	smooth = (1.0-smoothDegree) * (origin-bilateral)+bilateral;

}else{
smooth = origin;
}
gl_FragColor = smooth;

}