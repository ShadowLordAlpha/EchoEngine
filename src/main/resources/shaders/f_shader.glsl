#version 400 core

in vec2 pass_text_coords;

out vec4 out_color;

uniform sampler2D texture_sampler;

void main(void) {

	out_color = texture(texture_sampler, pass_text_coords);
}
