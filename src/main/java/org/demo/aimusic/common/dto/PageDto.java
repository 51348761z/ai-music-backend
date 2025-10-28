package org.demo.aimusic.common.dto;

import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PageDto<T> {
  private long currentPage;
  private long pageSize;
  private long total;
  private long totalPages;
  private List<T> list;

  private PageDto(IPage<T> page){
    this.currentPage = page.getCurrent();
    this.pageSize = page.getPages();
    this.total = page.getTotal();
    this.totalPages = page.getPages();
    this.list = page.getRecords();
  }

  /**
   * Static factory method to create PageDto from IPage.
   * @param page The {@link IPage} object returned from a MyBatis-Plus query.
   * @return A new {@link PageDto} instance.
   * @param <T> A new PageDTO instance.
   */
  public static <T> PageDto<T> fromPage(IPage<T> page){
    return new PageDto<>(page);
  }

}
