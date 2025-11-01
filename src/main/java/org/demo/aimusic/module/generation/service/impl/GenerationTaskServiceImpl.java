package org.demo.aimusic.module.generation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.demo.aimusic.module.generation.entity.GenerationTask;
import org.demo.aimusic.module.generation.mapper.GenerationTaskMapper;
import org.demo.aimusic.module.generation.service.GenerationTaskService;
import org.springframework.stereotype.Service;

@Service
public class GenerationTaskServiceImpl extends ServiceImpl<GenerationTaskMapper, GenerationTask>
    implements GenerationTaskService {

}
