package com.java.server.main.entity;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PersonFrom {

	
	 @NotEmpty
	    @Size(min = 2, max = 30)
	    private String name;

	    @NotNull
	    @Min(18)
	    private Integer age;
	    
	    @NotEmpty(message="enter yout work")
	    private String work;
}
