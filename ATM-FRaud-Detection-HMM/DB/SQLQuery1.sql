USE [HMM]
GO

/****** Object:  Table [dbo].[Account]    Script Date: 01/10/2014 12:32:26 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[Account](
	[Ac_number] [int] NULL,
	[Ac_type] [nvarchar](50) NULL,
	[Pin_code] [nvarchar](100) NULL,
	[Balance] [int] NULL,
	[RFID] [nvarchar](50) NULL,
	[SecurityCode] [nvarchar](50) NULL
) ON [PRIMARY]

GO


