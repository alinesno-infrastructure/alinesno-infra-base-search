package com.alinesno.infra.base.search.service;

import com.alinesno.infra.base.search.entity.DatasetKnowledgeEntity;
import com.alinesno.infra.common.facade.services.IBaseService;

import java.util.List;

/**
 * 数据集知识管理Service接口
 * 该接口提供了对数据集知识的基本操作，包括文档解析等功能
 * 主要用于处理数据集中的文档，提取并保存有用信息
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
public interface IDatasetKnowledgeService extends IBaseService<DatasetKnowledgeEntity> {

    /**
     * 文档解析功能
     * 该方法用于接收一个数据集ID和一组句子，将这些句子作为文档内容进行解析
     * 并根据文件名和文件类型对解析结果进行相应处理
     *
     * @param datasetId 数据集的唯一标识
     * @param sentenceList 待解析的句子列表，代表文档的内容
     * @param fileName 文件名，用于在解析过程中标识文档
     * @param fileType 文件类型，影响解析方式或存储格式
     */
    void parserDocument(Long datasetId, List<String> sentenceList, String fileName, String fileType);
}
