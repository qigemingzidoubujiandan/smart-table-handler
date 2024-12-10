package com.ruoyi.project.tool.parse.parser.ai;

import com.google.common.base.Objects;
import lombok.Data;

@Data
public class AIResponse {

   private String fileId;

   private String resp;

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      AIResponse that = (AIResponse) o;
      return Objects.equal(resp, that.resp);
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(resp);
   }
}
