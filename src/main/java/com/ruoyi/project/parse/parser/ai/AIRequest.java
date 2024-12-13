package com.ruoyi.project.parse.parser.ai;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.File;

@Data
@AllArgsConstructor
public class AIRequest {

   private File file;

   private String question;
}
