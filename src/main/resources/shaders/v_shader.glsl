#version 400 core

in vec3 position;
in vec2 text_coords;

out vec2 pass_text_coords;

void main(void) {
	
	gl_Position = vec4(position, 1.0);
	pass_text_coords = text_coords;
}
